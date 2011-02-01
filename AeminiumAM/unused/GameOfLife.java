// Game of Life v1.3
// Copyright 1996-2001 Edwin Martin <edwin@bitstorm.nl>
// version 1.0 online since July 3 1996
// Changes:

package unused;
// 1.1: Double buffering to screen; faster paint
// 1.2: Arrowkeys changed; better use of `synchronized'
// 1.3: Choose speed from drop down menu and draw with mouse

import java.awt.*;
import java.applet.*;

public class GameOfLife extends Applet implements Runnable
{
	private CellSpace cellSpace;
	private Thread gameThread = null;
	private int genTime;
	private final String clear = "Clear";
	private final String glider = "Glider";
	private final String exploder1 = "Small Exploder";
	private final String exploder2 = "Exploder";
	private final String row10 = "10 Cell Row";
	private final String fish = "Fish";
	private final String pump = "Pump";
	private final String gun = "Shooter";
	private final String slow = "Slow";
	private final String fast = "Fast";
	private final String hyper = "Hyper";
	private final String nextLabel = "Next";
	private final String startLabel = "Start";
	private final String stopLabel = "Stop";
	private Label genLabel;
	private Button startstopButton;

	public void init()
	{
		int cellSize;
		int cellCols;
		int cellRows;
		String param;

		// set background
		setBackground( new Color( 0x999999 ) );

		// read parameters from HTML
		param = getParameter("cellsize");
		if ( param == null) {
			cellSize = 11;
		} else
			cellSize = Integer.valueOf( param ).intValue();

		param = getParameter("cellcols");
		if ( param == null ) {
			cellCols = 40;
		} else
			cellCols = Integer.valueOf( param ).intValue();

		param = getParameter("cellrows");
		if ( param == null ) {
			cellRows = 25;
		} else
			cellRows = Integer.valueOf( param ).intValue();

		param = getParameter("gentime");
		if ( param == null ) {
			genTime = 500;
		} else
			genTime = Integer.valueOf( param ).intValue();

		// create components and add them to container
		cellSpace = new CellSpace( cellSize, cellCols, cellRows );

		Choice c = new Choice();
		c.addItem( clear );
		c.addItem( glider );
		c.addItem( exploder2 );
		c.addItem( exploder1 );
		c.addItem( row10 );
		c.addItem( fish );
		c.addItem( pump );
		c.addItem( gun );
                
                Choice speed = new Choice();
                speed.addItem( slow );
                speed.addItem( fast );
                speed.addItem( hyper );

		genLabel = new Label( "Generations: 0             " );

		startstopButton = new Button( startLabel );

		Panel controls = new Panel();
		controls.add( c );
		controls.add( new Button( nextLabel ));
		controls.add( startstopButton );
		controls.add( speed );
		controls.add( genLabel );

		setLayout(new BorderLayout());
		add( "South", controls );
		add( "North", cellSpace );
                show();
		resize( preferredSize() );
                validate();
	}

	// no start() to prevent starting immediately
	public void start2() {
		if(gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	public void stop() {
		if(gameThread != null) {
			gameThread.stop();
			gameThread = null;
		}
	}

	public void run() {
		while (gameThread != null) {
			cellSpace.next();
			cellSpace.repaint();
			showGenerations();
			try {
				gameThread.sleep( genTime );
			} catch (InterruptedException e){}
		}
	}

	public boolean action(Event evt, Object arg) {
		if( clear.equals( arg ) ) // clear
                {
                        cellSpace.clear();
                        cellSpace.repaint();
                        showGenerations();
                        return true;
                }
                else if( glider.equals( arg ) ) // misc shapes
                {
                        int shape[] = { 0,1, 1,2, 2,2, 2,1, 2,0 };
                        drawShape( 3, 3, shape );
                        return true;
                }
                else if( exploder1.equals( arg ) )
                {
                        int shape[] = { 0,1, 0,2, 1,0, 1,1, 1,3, 2,1, 2,2 };
                        drawShape( 3, 4, shape );
                        return true;
                }
                else if(exploder2.equals(arg))
                {
                        int shape[] = { 0,0, 0,1, 0,2, 0,3, 0,4, 2,0, 2,4, 4,0, 4,1, 4,2, 4,3, 4,4 };
                        drawShape( 5, 5, shape );
                        return true;
                }
                else if(row10.equals(arg))
                {
                        int shape[] = { 0,0, 1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0 };
                        drawShape( 10, 1, shape );
                        return true;
                }
                else if(fish.equals(arg))
                {
                        int shape[] = { 0,1, 0,3, 1,0, 2,0, 3,0, 3,3, 4,0, 4,1, 4,2 };
                        drawShape( 5, 4, shape );
                        return true;
                }
                else if(pump.equals(arg))
                {
                        int shape[] = { 0,3, 0,4, 0,5, 1,0, 1,1, 1,5, 2,0, 2,1, 2,2, 2,3, 2,4, 4,0, 4,1, 4,2, 4,3, 4,4, 5,0, 5,1, 5,5, 6,3, 6,4, 6,5 };
                        drawShape( 7, 6, shape );
                        return true;
                }
                else if(gun.equals(arg))
                {
                        int shape[] = { 0,2, 0,3, 1,2, 1,3, 8,3, 8,4, 9,2, 9,4, 10,2, 10,3, 16,4, 16,5, 16,6, 17,4, 18,5, 22,1, 22,2, 23,0, 23,2, 24,0, 24,1, 24,12, 24,13, 25,12, 25,14, 26,12, 34,0, 34,1, 35,0, 35,1, 35,7, 35,8, 35,9, 36,7, 37,8 };
                        drawShape( 38, 15, shape );
                        return true;
                }
                else if(nextLabel.equals(arg)) // next
                {
                        cellSpace.next();
                        cellSpace.repaint();
                        showGenerations();
                        return true;
                }
                else if(startLabel.equals(arg)) // start
                {
                        start2();
                        startstopButton.setLabel( stopLabel );
                        return true;
                }
                else if(stopLabel.equals(arg)) // stop
                {
                        stop();
                        startstopButton.setLabel( startLabel );
                        return true;
                }
                else if(slow.equals(arg)) // slow
                {
                        genTime = 500;
                        return true;
                }
                else if(fast.equals(arg)) // fast
                {
                        genTime = 100;
                        return true;
                }
                else if(hyper.equals(arg)) // hyperspeed
                {
                        genTime = 0;
                        return true;
                }
                return false;
            }

		public String getAppletInfo()
		{
			return "Game Of Life v. 1.3\nCopyright 1996-2001 Edwin Martin";
		}

		// show number of generations
		public void showGenerations() {
			genLabel.setText( "Generations: "+cellSpace.generations );
		}

		// draws the shape to canvas
		public void drawShape( int shapeWidth, int shapeHeight, int shape[] ) {
			if ( !cellSpace.drawShape( shapeWidth, shapeHeight, shape ) )
				showStatus( "Shape is too big to fit." );
			else {
				showStatus( "" );
				cellSpace.repaint();
				showGenerations();
			}
		}

}


class CellSpace extends Canvas
{
	public int generations;
	private int cellSize;
	private int cellRows;
	private int cellCols;
        private boolean cellUnderMouse;
	private boolean cells[][];
	private int cellsBuffer[][];
	private Image offScreenImage = null;
	private Graphics offScreenGraphics;

	public CellSpace( int cellSize, int cellCols, int cellRows ) {
		cells = new boolean[cellCols][cellRows];
		cellsBuffer = new int[cellCols][cellRows];
		this.cellSize = cellSize;
		this.cellCols = cellCols;
		this.cellRows = cellRows;
		reshape(0, 0, cellSize*cellCols-1, cellSize*cellRows-1);
		clear();
	}

	public synchronized boolean mouseUp(java.awt.Event evt, int x, int y) {
		// toggle cell
                try {
                    cells[x/cellSize][y/cellSize] = !cellUnderMouse;
                } catch ( java.lang.ArrayIndexOutOfBoundsException e ) {}
		repaint();
		return true;
	}

	public synchronized boolean mouseDown(java.awt.Event evt, int x, int y) {
                try {
                    cellUnderMouse = cells[x/cellSize][y/cellSize];
                } catch ( java.lang.ArrayIndexOutOfBoundsException e ) {}
		return true;
        }

	public synchronized boolean mouseDrag(java.awt.Event evt, int x, int y) {
		// toggle cell
                try {
        		cells[x/cellSize][y/cellSize] = !cellUnderMouse;
                } catch ( java.lang.ArrayIndexOutOfBoundsException e ) {}
		repaint();
		return true;
	}

        public synchronized void update( Graphics theG )
	{
		Dimension d = size();
		if((offScreenImage == null) ) {
			offScreenImage = createImage( d.width, d.height );
			offScreenGraphics = offScreenImage.getGraphics();
		}
		paint(offScreenGraphics);
		theG.drawImage( offScreenImage, 0, 0, null );
	}

	public void paint(Graphics g) {
		// draw background (MSIE doesn't do that)
		g.setColor( Color.gray );
		g.fillRect( 0, 0, cellSize*cellCols-1, cellSize*cellRows-1 );
		// draw grid
		g.setColor( getBackground() );
		for( int x=1; x<cellCols; x++ ) {
			g.drawLine( x*cellSize-1, 0, x*cellSize-1, cellSize*cellRows-1 );
		}
		for( int y=1; y<cellRows; y++ ) {
			g.drawLine( 0, y*cellSize-1, cellSize*cellCols-1, y*cellSize-1 );
		}
		// draw populated cells
		g.setColor( Color.yellow );
		for( int y=0; y<cellRows; y++ ) {
			for( int x=0; x<cellCols; x++ ) {
				if ( cells[x][y] ) {
					g.fillRect( x*cellSize, y*cellSize, cellSize-1, cellSize-1 );
				}
			}
		}
	}

	// clears canvas
	public synchronized void clear() {
		generations = 0;
		for( int x=0; x<cellCols; x++ ) {
			for( int y=0; y<cellRows; y++ ) {
				cells[x][y] = false;
			}
		}
	}

	// create next generation of shape
	public synchronized void next() {
		int x;
		int y;

		generations++;
		// clear the buffer
		for( x=0; x<cellCols; x++ ) {
			for( y=0; y<cellRows; y++ ) {
				cellsBuffer[x][y] = 0;
			}
		}

		// count neighbors of off-edge cells
		for( x=1; x<cellCols-1; x++ ) {
			for( y=1; y<cellRows-1; y++ ) {
				if ( cells[x][y] ) {
					cellsBuffer[x-1][y-1]++;
					cellsBuffer[x][y-1]++;
					cellsBuffer[x+1][y-1]++;
					cellsBuffer[x-1][y]++;
					cellsBuffer[x+1][y]++;
					cellsBuffer[x-1][y+1]++;
					cellsBuffer[x][y+1]++;
					cellsBuffer[x+1][y+1]++;
				}
			}
		}

		// count neighbors of edge cells
		x=1; // start at (1,0)
		y=0;
		int dx=1;
		int dy=0;
		while( true ) {
			if ( cells[x][y] ) {
				if ( x > 0 ) {
					if ( y > 0 )
						cellsBuffer[x-1][y-1]++;
					if ( y < cellRows-1 )
						cellsBuffer[x-1][y+1]++;
					cellsBuffer[x-1][y]++;
				}
				if ( x < cellCols-1 ) {
					if ( y < cellRows-1 )
						cellsBuffer[x+1][y+1]++;
					if ( y > 0 )
						cellsBuffer[x+1][y-1]++;
					cellsBuffer[x+1][y]++;
				}
				if ( y > 0 )
					cellsBuffer[x][y-1]++;
				if ( y < cellRows-1 )
					cellsBuffer[x][y+1]++;
			}

			// turn clockwise at collision with edge
			if ( x==cellCols-1 && y==0 ) {
				dx = 0;
				dy = 1;
			} else if ( x==cellCols-1 && y==cellRows-1 ) {
				dx = -1;
				dy = 0;
			} else if ( x==0 && y==cellRows-1 ) {
				dx = 0;
				dy = -1;
			} else if ( x==0 && y==0 ) {
				// all edge cells done
				break;
			}
			x += dx;
			y += dy;
		}

		// here is the life algorithm
		// simple, isn't it?
		for( x=0; x<cellCols; x++ ) {
			for( y=0; y<cellRows; y++ ) {
				switch( cellsBuffer[x][y] ) {
					case 2:
						// no change
						break;
					case 3:
						cells[x][y] = true;
						break;
					default:
						cells[x][y] = false;
						break;
				}
			}
		}

	}

	// draws shape in cells
	// returns false if shape doesn't fit
	public synchronized boolean drawShape( int shapeWidth, int shapeHeight, int shape[] ) {
		int xOffset;
		int yOffset;

		if ( shapeWidth>cellCols || shapeHeight>cellRows )
			return false; // shape doesn't fit on canvas

		// center the shape
		xOffset = (cellCols-shapeWidth)/2;
		yOffset = (cellRows-shapeHeight)/2;
		clear();
		for ( int i=0; i < shape.length; i+=2 )
			cells[xOffset+shape[i]][yOffset+shape[i+1]] = true;
		return true;
	}

}